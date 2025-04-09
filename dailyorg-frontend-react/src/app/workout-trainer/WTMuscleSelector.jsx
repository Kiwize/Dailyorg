import * as React from 'react';
import PropTypes from 'prop-types';
import useAutocomplete from '@mui/material/useAutocomplete';
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';
import { styled } from '@mui/material/styles';
import { autocompleteClasses } from '@mui/material/Autocomplete';
import useApi from '../../hooks/useApi';
import useAlert from '../../hooks/useAlert';
import { useEffect } from 'react';
import { useState } from 'react';

const API_URL = import.meta.env.VITE_API_URL;

const Root = styled('div')(({ theme }) => ({
    color: 'rgba(0,0,0,0.85)',
    fontSize: '14px',
    ...theme.applyStyles('dark', {
        color: 'rgba(255,255,255,0.65)',
    }),
}));

const Label = styled('label')`
  padding: 0 0 4px;
  line-height: 1.5;
  display: block;
`;

const InputWrapper = styled('div')(({ theme }) => ({
    width: '300px',
    border: '1px solid #d9d9d9',
    backgroundColor: '#fff',
    borderRadius: '4px',
    padding: '1px',
    display: 'flex',
    flexWrap: 'wrap',
    ...theme.applyStyles('dark', {
        borderColor: '#434343',
        backgroundColor: '#141414',
    }),
    '&:hover': {
        borderColor: '#40a9ff',
        ...theme.applyStyles('dark', {
            borderColor: '#177ddc',
        }),
    },
    '&.focused': {
        borderColor: '#40a9ff',
        boxShadow: '0 0 0 2px rgb(24 144 255 / 0.2)',
        ...theme.applyStyles('dark', {
            borderColor: '#177ddc',
        }),
    },
    '& input': {
        backgroundColor: '#fff',
        color: 'rgba(0,0,0,.85)',
        height: '30px',
        boxSizing: 'border-box',
        padding: '4px 6px',
        width: '0',
        minWidth: '30px',
        flexGrow: 1,
        border: 0,
        margin: 0,
        outline: 0,
        ...theme.applyStyles('dark', {
            color: 'rgba(255,255,255,0.65)',
            backgroundColor: '#141414',
        }),
    },
}));

function Tag(props) {
    const { label, onDelete, ...other } = props;
    return (
        <div {...other}>
            <span>{label}</span>
            <CloseIcon onClick={onDelete} />
        </div>
    );
}

Tag.propTypes = {
    label: PropTypes.string.isRequired,
    onDelete: PropTypes.func.isRequired,
};

const StyledTag = styled(Tag)(({ theme }) => ({
    display: 'flex',
    alignItems: 'center',
    height: '24px',
    margin: '2px',
    lineHeight: '22px',
    backgroundColor: '#fafafa',
    border: `1px solid #e8e8e8`,
    borderRadius: '2px',
    boxSizing: 'content-box',
    padding: '0 4px 0 10px',
    outline: 0,
    overflow: 'hidden',
    ...theme.applyStyles('dark', {
        backgroundColor: 'rgba(255,255,255,0.08)',
        borderColor: '#303030',
    }),
    '&:focus': {
        borderColor: '#40a9ff',
        backgroundColor: '#e6f7ff',
        ...theme.applyStyles('dark', {
            backgroundColor: '#003b57',
            borderColor: '#177ddc',
        }),
    },
    '& span': {
        overflow: 'hidden',
        whiteSpace: 'nowrap',
        textOverflow: 'ellipsis',
    },
    '& svg': {
        fontSize: '12px',
        cursor: 'pointer',
        padding: '4px',
    },
}));

const Listbox = styled('ul')(({ theme }) => ({
    width: '300px',
    margin: '2px 0 0',
    padding: 0,
    position: 'absolute',
    listStyle: 'none',
    backgroundColor: '#fff',
    overflow: 'auto',
    maxHeight: '250px',
    borderRadius: '4px',
    boxShadow: '0 2px 8px rgb(0 0 0 / 0.15)',
    zIndex: 1,
    ...theme.applyStyles('dark', {
        backgroundColor: '#141414',
    }),
    '& li': {
        padding: '5px 12px',
        display: 'flex',
        '& span': {
            flexGrow: 1,
        },
        '& svg': {
            color: 'transparent',
        },
    },
    "& li[aria-selected='true']": {
        backgroundColor: '#fafafa',
        fontWeight: 600,
        ...theme.applyStyles('dark', {
            backgroundColor: '#2b2b2b',
        }),
        '& svg': {
            color: '#1890ff',
        },
    },
    [`& li.${autocompleteClasses.focused}`]: {
        backgroundColor: '#e6f7ff',
        cursor: 'pointer',
        ...theme.applyStyles('dark', {
            backgroundColor: '#003b57',
        }),
        '& svg': {
            color: 'currentColor',
        },
    },
}));

export default function WTMuscleSelector({muscles}) {
    const [selectedMuscles, setselectedMuscles] = useState([]);

    const {
        getRootProps,
        getInputLabelProps,
        getInputProps,
        getTagProps,
        getListboxProps,
        getOptionProps,
        groupedOptions,
        value,
        focused,
        setAnchorEl,
    } = useAutocomplete({
        id: 'customized-hook-demo',
        defaultValue: selectedMuscles,
        multiple: true,
        options: muscles,
        getOptionLabel: (option) => option.name,
    });

    return (
        <Root>
            <div {...getRootProps()}>
                <Label {...getInputLabelProps()}>Muscles</Label>
                <InputWrapper ref={setAnchorEl} className={focused ? 'focused' : ''}>
                    {value.map((option, index) => {
                        const { key, ...tagProps } = getTagProps({ index });
                        return <StyledTag key={key} {...tagProps} label={option.name} />;
                    })}
                    <input {...getInputProps()} />
                </InputWrapper>
            </div>
            {groupedOptions.length > 0 ? (
                <Listbox {...getListboxProps()}>
                    {groupedOptions.map((option, index) => {
                        const { key, ...optionProps } = getOptionProps({ option, index });
                        return (
                            <li key={key} {...optionProps}>
                                <span>{option.name}</span>
                                <CheckIcon fontSize="small" />
                            </li>
                        );
                    })}
                </Listbox>
            ) : null}
        </Root>
    );
}
